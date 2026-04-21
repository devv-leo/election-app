# Voting Platform — Frontend Design Document

A minimal, phase-aware web client for a Java voting backend. Prioritizes clarity, irreversibility safety, and honest surfacing of backend quirks over visual flourish.

---

## 1. Design Principles

1. **Phase is the primary signal.** Every election screen communicates its lifecycle state (nomination / voting / closed) through icon + label + color — never color alone. A colorblind user on a grayscale monitor must still know what phase they're in.
2. **One decision per screen.** Linear flows beat dense dashboards. No modal-on-modal; destructive or irreversible actions use inline confirm, not dialogs.
3. **Irreversibility is sacred.** Votes never optimistically update. The UI waits for server confirmation and commits to the "already voted" state only after success.
4. **Progressive disclosure for power.** Admin actions (create election, activate, nominate) are hidden until the user's context calls for them. The voter-first surface stays uncluttered.
5. **Typography and spacing carry the design.** No gradients, no glass, no shadow stacks. Flat surfaces, hairline borders, one accent color, one typeface.
6. **Honest about state.** If the backend has a quirk (poll keyed by last name, no election title), the UI names it plainly rather than pretending it away.
7. **Copy is direct.** "Cast vote," "Add candidate," "End nominations." Never "Submit your choice" or "Proceed to next step."

---

## 2. Visual System

| Token | Value | Notes |
|---|---|---|
| **Font family** | Inter (system fallback: -apple-system, Segoe UI) | Single family, three weights |
| **Font weights** | 400 / 500 / 700 | Regular, medium, bold only |
| **Type scale** | 12 / 14 / 16 / 20 / 28 / 40 px | 16 is body; 28 is screen title; 40 only on auth |
| **Line height** | 1.5 body, 1.2 headings | |
| **Spacing scale** | 4 / 8 / 12 / 16 / 24 / 32 / 48 / 64 | 8-based, with 4 for tight inline cases |
| **Container width** | 640px (forms), 880px (lists/detail) | Never full-width on desktop |
| **Radius** | 6px (inputs, buttons), 8px (cards) | No fully rounded pills |
| **Borders** | 1px solid, used instead of shadow for elevation | |
| **Accent** | `#2F6FEB` (blue) | One color. Used for primary action + active phase marker only |
| **Neutral (light)** | bg `#FFFFFF`, surface `#FAFAFA`, border `#E5E5E5`, text `#111111`, muted `#6B6B6B` | |
| **Neutral (dark)** | bg `#0F0F10`, surface `#17181A`, border `#2A2B2E`, text `#F2F2F2`, muted `#9A9A9A` | Accent unchanged |
| **Phase — nomination** | `#8A5A00` on `#FFF7E6` (amber) + ◔ icon + "Nominations open" | |
| **Phase — voting** | `#2F6FEB` on `#EEF3FE` (blue) + ● icon + "Voting open" | Shares accent deliberately |
| **Phase — closed** | `#444444` on `#F0F0F0` (gray) + ▣ icon + "Closed" | |
| **Motion** | 120ms ease-out for hover, 160ms for state change, no entrance animations | |
| **Focus ring** | 2px accent, 2px offset | Visible on all interactive elements |
| **Dark mode** | Token swap only; no reworked layouts | Respects `prefers-color-scheme` + manual toggle |

Phase colors are paired with distinct icons and text labels on every appearance — the color is reinforcement, never the sole signal.

---

## 3. Information Architecture

```
/                         → redirects to /elections if logged in, else /login
/signup                   Public
/login                    Public
/elections                Authed — grouped list
/elections/new            Authed — admin action, disclosed from list
/elections/:slug          Authed — phase-aware detail
/elections/:slug/nominate Authed — only visible during nomination phase
/account                  Authed — profile + logout
```

**Slugs:** Backend has no title field. Client generates a readable slug from `createdAt` + short ID: `election-apr-20-a3f2`. Displayed as the election's de facto name everywhere. (See §9 for the backend fix.)

**Navigation:** Top bar with logo-mark on left, `Elections` link, and account avatar on right. No sidebar — the app isn't big enough to justify one.

---

## 4. Screen Designs

### 4.1 Auth — Signup / Login

Two near-identical screens. Tabs at top; form centered; nothing else on the page.

```
┌────────────────────────────────────────────────────┐
│                                                    │
│                    ◆ Ballot                        │
│                                                    │
│         ┌──────────────────────────────┐           │
│         │  [ Log in ]    Sign up       │           │
│         ├──────────────────────────────┤           │
│         │                              │           │
│         │  Email                       │           │
│         │  ┌────────────────────────┐  │           │
│         │  │                        │  │           │
│         │  └────────────────────────┘  │           │
│         │                              │           │
│         │  Password                    │           │
│         │  ┌────────────────────────┐  │           │
│         │  │                    👁   │  │           │
│         │  └────────────────────────┘  │           │
│         │                              │           │
│         │  ┌────────────────────────┐  │           │
│         │  │        Log in          │  │           │
│         │  └────────────────────────┘  │           │
│         │                              │           │
│         │  Forgot password? (disabled) │           │
│         └──────────────────────────────┘           │
│                                                    │
└────────────────────────────────────────────────────┘
```

Signup adds `First name` and `Last name` fields above email. Password field shows a subtle warning under it on signup: *"Use a password you don't use elsewhere."* (We're being honest — see §9.)

### 4.2 Elections List — Grouped by Phase

```
┌──────────────────────────────────────────────────────────┐
│ ◆ Ballot          Elections                    ○ Sade M. │
├──────────────────────────────────────────────────────────┤
│                                                          │
│  Elections                            [ + New election ] │
│                                                          │
│  ● Voting open ─────────────────────────────────────────  │
│                                                          │
│   ┌────────────────────────────────────────────────────┐ │
│   │ election-apr-18-7c1a                               │ │
│   │ 4 candidates · you haven't voted                 › │ │
│   └────────────────────────────────────────────────────┘ │
│                                                          │
│  ◔ Nominations open ────────────────────────────────────  │
│                                                          │
│   ┌────────────────────────────────────────────────────┐ │
│   │ election-apr-20-a3f2                               │ │
│   │ 2 candidates so far                              › │ │
│   └────────────────────────────────────────────────────┘ │
│                                                          │
│  ▣ Closed ──────────────────────────────────────────────  │
│                                                          │
│   ┌────────────────────────────────────────────────────┐ │
│   │ election-apr-10-9b4d                               │ │
│   │ Winner: Okafor · 142 votes                       › │ │
│   └────────────────────────────────────────────────────┘ │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

Groups collapse if empty (no "No elections in this phase" noise). `+ New election` is the only admin entry point on this screen — progressive disclosure means creating isn't a primary tab.

### 4.3 Election Detail — Nomination Phase

```
┌──────────────────────────────────────────────────────────┐
│ ◆ Ballot     ‹ Elections                        ○ Sade M.│
├──────────────────────────────────────────────────────────┤
│                                                          │
│  ◔ Nominations open                                      │
│  election-apr-20-a3f2                                    │
│  Created April 20, 2026                                  │
│                                                          │
│  Candidates (2)                                          │
│  ─────────────────────────────────────────               │
│   Ada Okafor                                             │
│   Tomi Balogun                                           │
│  ─────────────────────────────────────────               │
│                                                          │
│  ┌──────────────────────┐  ┌──────────────────────┐      │
│  │  + Nominate          │  │  End nominations     │      │
│  └──────────────────────┘  └──────────────────────┘      │
│                             (admin — starts voting)      │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

`End nominations` triggers activation. Since any logged-in voter is technically an admin, we keep the button visible but label the consequence inline. Tapping it opens an inline confirm strip (no modal):

```
  ┌────────────────────────────────────────────────────┐
  │ Start voting? Nominations will close. [Cancel][Start]│
  └────────────────────────────────────────────────────┘
```

### 4.4 Election Detail — Voting, Not Yet Voted

```
┌──────────────────────────────────────────────────────────┐
│ ◆ Ballot     ‹ Elections                        ○ Sade M.│
├──────────────────────────────────────────────────────────┤
│                                                          │
│  ● Voting open                                           │
│  election-apr-18-7c1a                                    │
│                                                          │
│  Choose one candidate. You cannot change your vote.      │
│                                                          │
│  ┌────────────────────────────────────────────────────┐  │
│  │ ○  Ada Okafor                                      │  │
│  ├────────────────────────────────────────────────────┤  │
│  │ ●  Tomi Balogun                                    │  │
│  ├────────────────────────────────────────────────────┤  │
│  │ ○  Chidi Eze                                       │  │
│  ├────────────────────────────────────────────────────┤  │
│  │ ○  Ngozi Adebayo                                   │  │
│  └────────────────────────────────────────────────────┘  │
│                                                          │
│  ┌──────────────────────────────────────────────────┐    │
│  │   Cast vote for Tomi Balogun                     │    │
│  └──────────────────────────────────────────────────┘    │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

**Two-tap inline confirm (not a modal):** First tap on `Cast vote` transforms the button into a confirm strip in place:

```
  ┌──────────────────────────────────────────────────┐
  │  Confirm vote for Tomi Balogun — this is final   │
  │             [ Cancel ]   [ Confirm ]             │
  └──────────────────────────────────────────────────┘
```

Only after `Confirm` does the request fire. No optimistic update: the button enters a loading state, then on success the whole screen transitions to the "already voted" state (§4.5). On failure, the strip returns with an inline error.

### 4.5 Election Detail — Voting, Already Voted

```
┌──────────────────────────────────────────────────────────┐
│ ◆ Ballot     ‹ Elections                        ○ Sade M.│
├──────────────────────────────────────────────────────────┤
│                                                          │
│  ● Voting open                                           │
│  election-apr-18-7c1a                                    │
│                                                          │
│  ✓ You voted for Tomi Balogun                            │
│  Your vote is recorded and cannot be changed.            │
│                                                          │
│  Current results                                         │
│  ─────────────────────────────────────────               │
│   Tomi Balogun     ████████████████  48                  │
│   Ada Okafor       ██████████        31                  │
│   Chidi Eze        █████             14                  │
│   Ngozi Adebayo    ██                 6                  │
│  ─────────────────────────────────────────               │
│   Updated just now · Refresh                             │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

Poll data comes back keyed by `lastName`. For most cases that's fine, but duplicates are a known bug (§9) — during nomination we block it client-side, and in results we append a disambiguating first-name-initial only if the backend returns a collision.

### 4.6 Election Detail — Closed

```
┌──────────────────────────────────────────────────────────┐
│ ◆ Ballot     ‹ Elections                        ○ Sade M.│
├──────────────────────────────────────────────────────────┤
│                                                          │
│  ▣ Closed                                                │
│  election-apr-10-9b4d                                    │
│                                                          │
│  Final results                                           │
│  ─────────────────────────────────────────               │
│   Okafor       ████████████████████  142   Winner        │
│   Balogun      ██████████             76                 │
│   Eze          █████                  38                 │
│  ─────────────────────────────────────────               │
│   Total votes: 256                                       │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

No voting controls. Winner badge uses text, not color alone.

### 4.7 Nominate Candidate Form

```
┌──────────────────────────────────────────────────────────┐
│ ◆ Ballot     ‹ election-apr-20-a3f2              ○ Sade M│
├──────────────────────────────────────────────────────────┤
│                                                          │
│  Nominate a candidate                                    │
│                                                          │
│  First name                                              │
│  ┌────────────────────────────────────────┐              │
│  │                                        │              │
│  └────────────────────────────────────────┘              │
│                                                          │
│  Last name                                               │
│  ┌────────────────────────────────────────┐              │
│  │                                        │              │
│  └────────────────────────────────────────┘              │
│  A candidate with this last name already exists.         │
│  Use a different last name to avoid a results conflict.  │
│                                                          │
│  ┌────────────────────────────────────────┐              │
│  │          Add candidate                 │              │
│  └────────────────────────────────────────┘              │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

Last-name collision is caught client-side before submit. Copy explains *why* (honest about the backend's last-name keying) without drowning the user in detail.

### 4.8 Account

```
┌──────────────────────────────────────────────────────────┐
│ ◆ Ballot     ‹ Elections                        ○ Sade M.│
├──────────────────────────────────────────────────────────┤
│                                                          │
│  Account                                                 │
│                                                          │
│  Sade Makinde                                            │
│  sade@example.com                                        │
│  Member since April 12, 2026                             │
│                                                          │
│  ─────────────────────────────────────────               │
│                                                          │
│  ┌──────────────────────┐                                │
│  │      Log out         │                                │
│  └──────────────────────┘                                │
│                                                          │
│  Theme: ( ) Light  ( ) Dark  (•) System                  │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

Deliberately sparse. No profile editing — backend doesn't support it, so we don't lie about it.

---

## 5. Component Library — Shortlist

Only components that actually appear:

- **Button** — primary, secondary, danger; single size
- **Text input** — label above, helper/error text below
- **Password input** — same as text input with visibility toggle
- **Radio list** — used for candidate selection (one card per option)
- **Phase badge** — icon + label + color token
- **Election card** — used on list screen
- **Inline confirm strip** — replaces button in-place; used for vote and for ending nominations
- **Horizontal bar chart** — results display, pure CSS
- **Top bar** — logo, single nav link, account avatar
- **Tabs** — only on auth screen
- **Toast** — bottom-center, auto-dismiss, used for non-blocking confirmations

No modals. No dropdowns. No accordions. No tooltips (labels are always visible).

---

## 6. UX Details

| Concern | Approach |
|---|---|
| **Loading** | Skeleton rows for lists (3–5 placeholders); button → spinner label for submits. No full-page spinners except initial session check. |
| **Errors** | Inline under the relevant input or action. Toast only for network-level failures ("Can't reach server. Retry."). Errors never replace content — the user's data stays visible. |
| **Optimistic updates** | Enabled for: logout, ending nominations. **Disabled for: casting a vote** (irreversible; wait for server). |
| **Keyboard** | Tab order follows visual order. Enter submits focused form. Arrow keys move between radio candidates. Escape cancels inline confirm strips. All buttons focusable with visible ring. |
| **Accessibility** | WCAG 2.2 AA: 4.5:1 text contrast, 3:1 non-text. Phase signaled by icon + text + color — never color alone. `aria-live="polite"` for results updates. Form errors linked via `aria-describedby`. Radio group uses real `<input type="radio">` with fieldset/legend. |
| **Copy voice** | Direct, short, no hedging. "Cast vote." "Add candidate." "End nominations." Confirmations name the consequence: *"this is final."* |
| **Time format** | Relative for recent ("2 minutes ago"), absolute for older ("April 10, 2026"). `<time>` with `datetime` attribute. Local timezone, 24h in Europe, 12h elsewhere — auto-detected. |
| **Session handling** | On app load, ping a lightweight `/voters/me` (see §9) to verify `isLoggedIn`. On 401 or missing session, redirect to `/login` and preserve intended route as `?next=`. On logout, clear client state immediately, then fire request. |
| **Empty states** | One line of text + one action. "No elections yet. Create one." — no illustrations. |
| **Destructive confirmations** | Inline strip, never a dialog. Must name the specific thing ("Confirm vote for Tomi Balogun") not a generic "Are you sure?" |
| **Offline / network loss** | Detect via `navigator.onLine` + failed requests. Show a persistent banner: "You're offline. Changes won't save." Disable submit buttons. |

---

## 7. End-to-End Flows

**Voter flow (happy path)**

Lands on `/` → redirected to `/login` → enters credentials → lands on `/elections` → sees a card under "Voting open" → taps through to detail → reads one-line instruction → selects a candidate radio → taps `Cast vote` → inline confirm appears → taps `Confirm` → button shows loading → screen transitions to "already voted" state with results bar chart.

**Admin flow (create + run an election)**

From `/elections`, taps `+ New election` → confirmation strip ("Create a new election? You'll add candidates next.") → confirms → lands on the new election's detail in nomination phase → taps `+ Nominate` → fills first/last name → submits → returns to detail with candidate added → repeats for each candidate → when ready, taps `End nominations` → inline confirm → election flips to voting phase → admin can now vote like any other voter. When voting should close, backend has no explicit "close" endpoint (see §9); current workaround is that "closed" is inferred when `isActive` has been toggled off, but there's no endpoint to toggle it off, which we flag honestly.

**Returning voter flow**

Lands on `/` → session check → redirected to `/elections` → sees existing elections grouped by phase → can revisit any voted election to see live results but not change vote.

---

## 8. Recommended Frontend Stack

- **Framework:** React + TypeScript (Vite). Boring, well-supported, matches team scale.
- **Routing:** React Router v6.
- **Data fetching:** TanStack Query. Handles caching, retries, background refresh of poll results cleanly.
- **Forms:** React Hook Form + Zod. Client-side validation for the last-name collision rule.
- **Styling:** CSS Modules with CSS variables for tokens. Tailwind is fine if the team prefers, but the design is so restrained that hand-written CSS is more honest. No component library — this design is too specific to benefit from Material/Chakra/etc.
- **Icons:** Lucide (phase icons, account avatar, chevrons). One icon set only.
- **State:** React context for session + theme. Everything else is server state via TanStack Query.
- **Testing:** Vitest + React Testing Library for units; Playwright for the vote flow (the one thing that absolutely must not break).
- **Deployment:** Static build served by the Spring Boot backend or a CDN.

---

## 9. Backend Gaps That Hurt UX

| Gap | UX symptom | Recommended fix |
|---|---|---|
| **No election title** | Users can't tell elections apart; we generate slugs as a crutch | Add `title: String` to Election; make it required at creation. |
| **Poll keyed by `lastName`** | Two candidates with the same last name corrupt results | Key poll by candidate ID; return `{ candidateId, firstName, lastName, count }` tuples. |
| **No "close voting" endpoint** | No way for admin to end an election cleanly | Add `PATCH /elections/{id}/close` that sets `isActive=false` and freezes votes. |
| **Any logged-in voter is an admin** | Anyone can create, activate, or end elections | Add a `role` field (`VOTER` / `ADMIN`) on Voter; gate admin endpoints. |
| **No `/voters/me` endpoint** | Client can't verify session without re-logging-in or looking up by email | Add `GET /voters/me` returning the current voter based on session. |
| **`isLoggedIn` as auth** | Same voter logged in from two devices logs the other out; no real session identity | Issue a session token (even a simple server-side session cookie) instead of a boolean flag. |
| **Plaintext passwords** | Catastrophic on breach; we warn users on signup but can't fix it in UI | Hash with BCrypt/Argon2 server-side. Non-negotiable before any production use. |
| **No "has this voter voted" endpoint** | Client has to infer voted state from the polls response or from local cache, which is fragile across devices | Add `GET /elections/{id}/myVote` returning the voter's vote for that election (or 404). |
| **No pagination on elections** | List grows unbounded | Add `?page=&size=` to the elections list endpoint. |
| **`POST /voters/logout` takes an email in the body** | Trivially spoofable — anyone can log anyone out | Derive identity from session, not request body. |
| **No duplicate-candidate guard server-side** | Client-side block only; a second client can still create the conflict | Add uniqueness check on `(electionId, lastName)` — or better, on `(electionId, candidateId)` once the poll-keying bug is fixed. |
| **`voterID` vs `voterId` inconsistency** | Minor, but any API client will trip on it | Normalize casing across all endpoints. |

---

The design leans on the assumption that we're honest about what the backend is and isn't — generating slugs, warning on signup, blocking last-name collisions client-side — rather than hiding the rough edges behind polish. Fixing the items in §9 lets us delete workarounds, not add features.